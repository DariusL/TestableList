package lt.dariusl.testablelist

class ColorPresenter (private val adapter: ColorAdapter, initialValues: List<RowModel>){
    private val viewModels = mutableListOf<TestableListActivity.ColorViewModel>()
    val items: List<TestableListActivity.ColorViewModel> = viewModels

    init {
        adapter.presenter = this
        insert(initialValues)
    }

    private fun insert(rows: List<RowModel>) {
        rows
                .sortedByDescending { it.priority }
                .forEach {
                    viewModels.add(TestableListActivity.ColorViewModel(it.priority, 1, it.color, false))
                    adapter.notifyItemInserted(viewModels.size - 1)
                }
    }
}

