package com.example.eventmatics.SwipeGesture

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmatics.R
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

abstract class SwipeToDelete(context:Context):ItemTouchHelper.SimpleCallback(
    0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {

    private val deleteColor=ContextCompat.getColor(context, R.color.Light_Pink)
    private val deleteicon=R.drawable.delete

    private val archiveColor=ContextCompat.getColor(context, R.color.light_blue_50)
    private val archiveicon=R.drawable.completed

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
            .addSwipeLeftBackgroundColor(deleteColor)
            .addSwipeLeftActionIcon(deleteicon)
            .addSwipeRightBackgroundColor(archiveColor)
            .addSwipeRightActionIcon(archiveicon)
            .create()
            .decorate()
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}