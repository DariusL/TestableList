package lt.dariusl.testablelist

import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import java.util.*

class TestableListActivity : AppCompatActivity() {

    @BindView(R.id.list_recycler)
    internal lateinit var recyclerView: RecyclerView

    private lateinit var presenter: ColorPresenter

    private val random = Random()

    private val colorIds = listOf(R.color.list_red, R.color.list_green, R.color.list_blue, R.color.list_yellow)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_list)
        ButterKnife.bind(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ColorAdapterImpl()
        recyclerView.adapter = adapter
        presenter = ColorPresenter(adapter, listOf(
                RowModel(5, color(R.color.list_red)),
                RowModel(3, color(R.color.list_green)),
                RowModel(3, color(R.color.list_green)),
                RowModel(1, color(R.color.list_blue))),
                savedInstanceState
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.saveState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                addItem()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun addItem() {
        presenter.insert(RowModel(getRandomPriority(), getRandomColor()))
    }

    @ColorInt
    private fun color(@ColorRes color: Int) : Int {
        return resources.getColor(color)
    }

    data class ColorViewModel(
            val priority: Int,
            val size: Int,
            @ColorInt val color: Int
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

    private fun getRandomPriority() = random.nextInt(50)

    private fun getRandomColor() = color(colorIds[random.nextInt(3)])

}
