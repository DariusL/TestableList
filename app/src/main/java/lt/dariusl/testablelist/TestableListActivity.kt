package lt.dariusl.testablelist

import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife

class TestableListActivity : AppCompatActivity() {

    @BindView(R.id.list_recycler)
    internal lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_list)
        ButterKnife.bind(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ColorAdapterImpl()
        recyclerView.adapter = adapter
        ColorPresenter(adapter, listOf(
                RowModel(5, color(R.color.list_red)),
                RowModel(3, color(R.color.list_green)),
                RowModel(3, color(R.color.list_green)),
                RowModel(1, color(R.color.list_blue)))
        )
    }

    @ColorInt
    private fun color(@ColorRes color: Int) : Int {
        return resources.getColor(color)
    }

    data class ColorViewModel(
            val priority: Int,
            val size: Int,
            @ColorInt val color: Int,
            val bottomDivider: Boolean
    )

    inner class ColorAdapterImpl() : RecyclerView.Adapter<ColorAdapterImpl.ColorViewHolder>(), ColorAdapter {

        override lateinit var presenter: ColorPresenter

        override fun getItemCount(): Int {
            return presenter.items.size
        }

        override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
            holder.bind(presenter.items[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ColorViewHolder {
            return ColorViewHolder(layoutInflater.inflate(R.layout.item_row, parent, false))
        }

        inner class ColorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            @BindView(R.id.row_priority)
            internal lateinit var priority: TextView
            @BindView(R.id.row_group_size)
            internal lateinit var groupSize: TextView

            init {
                ButterKnife.bind(this, itemView)
            }

            fun bind(model: ColorViewModel) {
                priority.text = model.priority.toString()
                groupSize.text = model.size.toString()
                itemView.setBackgroundColor(model.color)

            }
        }
    }

}
