package com.moon.astromicon.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.moon.astromicon.R
import com.moon.astromicon.models.MoonPhases
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.lang.Math.abs
import java.lang.Math.max
import java.util.*

var errorMessage: String = ""



fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, backStackTag: String? = null) {
    supportFragmentManager.inTransaction {
        add(frameId, fragment)
        backStackTag?.let { addToBackStack(fragment.javaClass.name) }
    }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, backStackTag: String? = null) {
    supportFragmentManager.inTransaction {
        replace(frameId, fragment)
        backStackTag?.let { addToBackStack(fragment.javaClass.name) }
    }
}

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}


fun setPinkBorder(imageViews: Array<View>, cornerRadius:Float){
    val border = GradientDrawable()
    //border.setColor(-0x1) //white background
    border.cornerRadius = cornerRadius

    border.setStroke(6, Color.parseColor("#FC0047")) //black border with full opacity

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        for (e in imageViews){
            e.foreground = border
        }

    }
}

fun hideKeyboard(context: Context, view: View){
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}


@RequiresApi(Build.VERSION_CODES.M)
fun removeBorder(view: View){
    view.foreground = null
}

fun formatPhoneString(phoneNum: String): String {

    var tempPhoneNum = phoneNum

    if (phoneNum.length == 11){
        if (phoneNum[0]=='8'){
            val sb = StringBuilder(phoneNum).also { it.setCharAt(0, '7') }
            tempPhoneNum = sb.toString()
        }

    }


    return if (tempPhoneNum.length == 11) {
        "+" +
                tempPhoneNum.substring(0, 1) +
                " " +
                tempPhoneNum.substring(1, 4) +
                " " +
                tempPhoneNum.substring(4, 7) +
                "-" +
                tempPhoneNum.substring(7, 9) +
                "-" +
                tempPhoneNum.substring(9, 11)
    } else {
        tempPhoneNum
    }

}


fun formatCarNumber(number: String): String{
    return try {
        if (number!=""){
            number.substring(0,1) +
                    " " +
                    number.substring(1,4)+
                    " " +
                    number.substring(4,6)
        } else ""

    } catch (e:Exception){
        ""
    }




}

class MyPasswordTransformationMethod : PasswordTransformationMethod() {
    override fun getTransformation(source: CharSequence?, view: View?): CharSequence {
        return PasswordCharSequence(source!!)
    }

    private inner class PasswordCharSequence(private val mSource: CharSequence) : CharSequence {

        override val length: Int
            get() = mSource.length

        override fun get(index: Int): Char {
            return '·'
        }

        override fun subSequence(start: Int, end: Int): CharSequence {
            return mSource.subSequence(start, end) // Return default
        }
    }
}

abstract class SwipeHelper(
    private val recyclerView: RecyclerView
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.ACTION_STATE_IDLE,
    ItemTouchHelper.LEFT
) {
    private var swipedPosition = -1
    private val buttonsBuffer: MutableMap<Int, List<UnderlayButton>> = mutableMapOf()
    private val recoverQueue = object : LinkedList<Int>() {
        override fun add(element: Int): Boolean {
            if (contains(element)) return false
            return super.add(element)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private val touchListener = View.OnTouchListener { _, event ->
        if (swipedPosition < 0) return@OnTouchListener false
        buttonsBuffer[swipedPosition]?.forEach { it.handle(event) }
        recoverQueue.add(swipedPosition)
        swipedPosition = -1
        recoverSwipedItem()
        true
    }

    init {
        recyclerView.setOnTouchListener(touchListener)
    }

    private fun recoverSwipedItem() {
        while (!recoverQueue.isEmpty()) {
            val position = recoverQueue.poll() ?: return
            recyclerView.adapter?.notifyItemChanged(position)
        }
    }

    private fun drawButtons(
        canvas: Canvas,
        buttons: List<UnderlayButton>,
        itemView: View,
        dX: Float
    ) {
        var right = itemView.right
        buttons.forEach { button ->
            val width = button.intrinsicWidth / buttons.intrinsicWidth() * abs(dX)
            val left = right - width
            button.draw(
                canvas,
                RectF(left, itemView.top.toFloat(), right.toFloat(), itemView.bottom.toFloat())
            )

            right = left.toInt()
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val position = viewHolder.adapterPosition
        var maxDX = dX
        val itemView = viewHolder.itemView

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                if (!buttonsBuffer.containsKey(position)) {
                    buttonsBuffer[position] = instantiateUnderlayButton(position)
                }

                val buttons = buttonsBuffer[position] ?: return
                if (buttons.isEmpty()) return
                maxDX = max(-buttons.intrinsicWidth(), dX)
                drawButtons(c, buttons, itemView, maxDX)
            }
        }

        super.onChildDraw(
            c,
            recyclerView,
            viewHolder,
            maxDX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        if (swipedPosition != position) recoverQueue.add(swipedPosition)
        swipedPosition = position
        recoverSwipedItem()
    }

    abstract fun instantiateUnderlayButton(position: Int): List<UnderlayButton>

    //region UnderlayButton
    interface UnderlayButtonClickListener {
        fun onClick()
    }

    class UnderlayButton(
        private val context: Context,
        private val title: String,
        textSize: Float,
        @ColorRes private val colorRes: Int,
        private val clickListener: UnderlayButtonClickListener
    ) {
        private var clickableRegion: RectF? = null
        private val textSizeInPixel: Float = textSize * context.resources.displayMetrics.density // dp to px
        private val horizontalPadding = 50.0f
        val intrinsicWidth: Float

        init {
            val paint = Paint()
            paint.textSize = textSizeInPixel
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.textAlign = Paint.Align.LEFT
            val titleBounds = Rect()
            paint.getTextBounds(title, 0, title.length, titleBounds)
            intrinsicWidth = titleBounds.width() + 2 * horizontalPadding
        }

        fun draw(canvas: Canvas, rect: RectF) {
            val paint = Paint()

            // Draw background
            paint.color = ContextCompat.getColor(context, colorRes)
            canvas.drawRect(rect, paint)

            // Draw title
            paint.color = ContextCompat.getColor(context, android.R.color.white)
            paint.textSize = textSizeInPixel
            paint.typeface = Typeface.DEFAULT_BOLD
            paint.textAlign = Paint.Align.LEFT

            val titleBounds = Rect()
            paint.getTextBounds(title, 0, title.length, titleBounds)

            val y = rect.height() / 2 + titleBounds.height() / 2 - titleBounds.bottom
            canvas.drawText(title, rect.left + horizontalPadding, rect.top + y, paint)

            clickableRegion = rect
        }

        fun handle(event: MotionEvent) {
            clickableRegion?.let {
                if (it.contains(event.x, event.y)) {
                    clickListener.onClick()
                }
            }
        }
    }

}

private fun List<SwipeHelper.UnderlayButton>.intrinsicWidth(): Float {
    if (isEmpty()) return 0.0f
    return map { it.intrinsicWidth }.reduce { acc, fl -> acc + fl }
}


fun getMoonPhase(d: Double): MoonPhases {

   return when (d) {
        in -180.0..-160.0 -> MoonPhases.NEWMOON
        in 160.0..180.0 -> MoonPhases.NEWMOON
        in -160.0..-20.0 -> MoonPhases.WAXING_CRESCENT
        in -20.0..20.0 -> MoonPhases.FULLMOON
        else -> MoonPhases.WANING_MOON
    }
}

fun formatPickedDateTime(zonedDateTime: java.time.ZonedDateTime?): String{
    val formatter = java.time.format.DateTimeFormatter.ofPattern("dd MMMM HH:mm")
    return  zonedDateTime!!.format(formatter)
}
enum class Phase(
    /**
     * Returns the moons's angle in reference to the sun, in degrees.
     */
    val angle: Double,
    val customName: String
) {
    /**
     * New moon.
     */
    NEW_MOON(0.0, "Новолуние"),

    /**
     * Waxing crescent moon.
     *
     * @since 3.5
     */
    WAXING_CRESCENT(45.0, "Растущая луна"),

    /**
     * Waxing half moon.
     */
    FIRST_QUARTER(90.0, "Первая четверть"),

    /**
     * Waxing gibbous moon.
     *
     * @since 3.5
     */
    WAXING_GIBBOUS(135.0, "Растущая луна"),

    /**
     * Full moon.
     */
    FULL_MOON(180.0, "Полнолуние"),

    /**
     * Waning gibbous moon.
     *
     * @since 3.5
     */
    WANING_GIBBOUS(225.0, "Стареющая луна"),

    /**
     * Waning half moon.
     */
    LAST_QUARTER(270.0, "Последняя четверть"),

    /**
     * Waning crescent moon.
     *
     * @since 3.5
     */
    WANING_CRESCENT(315.0, "Стареющая луна");

    /**
     * Returns the moons's angle in reference to the sun, in radians.
     */
    val angleRad: Double

    companion object {
        /**
         * Converts an angle to the closest matching moon phase.
         *
         * @param angle
         * Moon phase angle, in degrees. 0 = New Moon, 180 = Full Moon. Angles
         * outside the [0,360) range are normalized into that range.
         * @return Closest Phase that is matching that angle.
         * @since 3.5
         */
        fun toPhase(angle: Double): Phase {
            // bring into range 0.0 .. 360.0
            var normalized = angle % 360.0
            if (normalized < 0.0) {
                normalized += 360.0
            }
            if (normalized < 22.5) {
                return NEW_MOON
            }
            if (normalized < 67.5) {
                return WAXING_CRESCENT
            }
            if (normalized < 112.5) {
                return FIRST_QUARTER
            }
            if (normalized < 157.5) {
                return WAXING_GIBBOUS
            }
            if (normalized < 202.5) {
                return FULL_MOON
            }
            if (normalized < 247.5) {
                return WANING_GIBBOUS
            }
            if (normalized < 292.5) {
                return LAST_QUARTER
            }
            return if (normalized < 337.5) {
                WANING_CRESCENT
            } else NEW_MOON
        }
    }

    init {
        angleRad = Math.toRadians(angle)
    }



}
