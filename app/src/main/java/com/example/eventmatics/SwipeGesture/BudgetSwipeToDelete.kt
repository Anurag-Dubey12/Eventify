package com.example.eventmatics.SwipeGesture

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class BudgetSwipeToDelete(context: Context):ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT)
{
    private val deleteicon= R.drawable.baseline_delete_24
    private val Paidicon=R.drawable.paid

    private val DeleteColor=ContextCompat.getColor(context,R.color.Light_Pink)
    private val PaidColor=ContextCompat.getColor(context,R.color.light_blue_50)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
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
        RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftBackgroundColor(DeleteColor)
            .addSwipeLeftLabel("Delete")
//            .addSwipeRightBackgroundColor(PaidColor)
            .addSwipeLeftActionIcon(deleteicon)
//            .addSwipeRightLabel("Paid")
//            .addSwipeRightActionIcon(Paidicon)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}