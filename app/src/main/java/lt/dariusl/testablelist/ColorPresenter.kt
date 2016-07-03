package lt.dariusl.testablelist

import android.os.Bundle

class ColorPresenter (private val adapter: ColorAdapter, initialValues: List<RowModel>, state: Bundle?){

    private val viewModels = mutableListOf<TestableListActivity.ColorViewModel>()
    val items: List<TestableListActivity.ColorViewModel> = viewModels

    init {
        adapter.presenter = this
        if (state == null) {
            insert(initialValues)
        } else {

        }
    }

    private fun insert(rows: List<RowModel>) {
        viewModels.addAll(
                rows
                        .sortedByDescending { it.priority }
                        .map { TestableListActivity.ColorViewModel(it.priority, 1, it.color) }
        )
        setSizes()
        adapter.notifyDataSetChanged()
    }

    fun insert(row: RowModel) {
        viewModels.add(TestableListActivity.ColorViewModel(row.priority, 1, row.color))
        viewModels.sortByDescending { it.priority }
        setSizes()
        adapter.notifyDataSetChanged()
    }

    fun saveState(outState: Bundle) {
        
    }

    private fun setSizes() {
        if(viewModels.size == 0){
            return
        }
        var start = 0
        while (isInBounds(start)){
            val end = findGroup(start)
            val size = end - start
            for (position in start..end - 1){
                viewModels[position] = viewModels[position].copy(size = size)
            }
            start = end
        }
    }

    private fun findGroup(start: Int) : Int {
        var end = start
        while (isInBounds(end) && viewModels[start].color == viewModels[end].color) {
            end++
        }
        return end
    }

    private fun isInBounds(position: Int): Boolean {
        return position >= 0 && position < viewModels.size
    }
}

