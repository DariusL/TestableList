package lt.dariusl.testablelist

interface ColorAdapter {
    var presenter: ColorPresenter

    fun notifyItemInserted(position: Int)
    fun notifyDataSetChanged()
}