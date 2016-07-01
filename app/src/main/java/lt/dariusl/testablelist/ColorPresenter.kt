package lt.dariusl.testablelist

import android.support.annotation.ColorInt
import java.util.*

class ColorPresenter (private val adapter: ColorAdapter, initialValues: List<RowModel>){
    val viewModels = mutableListOf<TestableListActivity.ColorViewModel>()

    init {
        adapter.presenter = this
        insert(initialValues)
    }

    private fun insert(rows: List<RowModel>) {
        rows
                .sortedBy { it.priority }
                .forEach {
                    viewModels.add(TestableListActivity.ColorViewModel(it.priority, 1, it.color, false))
                    adapter.notifyItemInserted(viewModels.size - 1)
                }
    }

    val items: List<TestableListActivity.ColorViewModel> = viewModels
}

