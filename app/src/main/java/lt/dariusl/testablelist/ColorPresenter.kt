package lt.dariusl.testablelist

import java.text.FieldPosition

class ColorPresenter (private val adapter: ColorAdapter, initialValues: List<RowModel>){
    private val viewModels = mutableListOf<TestableListActivity.ColorViewModel>()
    val items: List<TestableListActivity.ColorViewModel> = viewModels

    init {
        adapter.presenter = this
        insert(initialValues)
    }

    private fun insert(rows: List<RowModel>) {
        viewModels.addAll(
                rows
                        .sortedByDescending { it.priority }
                        .map { TestableListActivity.ColorViewModel(it.priority, 1, it.color, false) }
        )
        setSizesAndDividers()
        adapter.notifyDataSetChanged()
    }

    private fun setSizesAndDividers() {
        if(viewModels.size == 0){
            return
        }
        var start = 0
        while (isInBounds(start)){
            val end = findGroup(start)
            val size = end - start
            for (position in start..end - 1){
                viewModels[position] = viewModels[position].copy(size = size, bottomDivider = position != end - 1)
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

