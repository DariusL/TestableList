package lt.dariusl.testablelist

import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class ColorPresenterTest {

    val red = 0x00FF0000
    val green = 0x0000FF00
    val blue = 0x000000FF

    val adapter = MockAdapter()

    @Test
    fun testInitialize() {
        val presenter = ColorPresenter(adapter, emptyList())
        assertThat(adapter.presenter, `is`(presenter))
        assertThat(adapter.models, `is`(mutableListOf()))
    }

    @Test
    fun testInsertSingleInitialValue() {
        ColorPresenter(adapter, listOf(RowModel(1, red)))
        assertThat(adapter.models, `is`(listOf(TestableListActivity.ColorViewModel(1, 1, red, false))))
    }

    @Test
    fun testInitialValuesAreSorted() {
        ColorPresenter(adapter, listOf(RowModel(2, red), RowModel(5, blue)))
        assertThat(adapter.models, `is`(listOf(
                TestableListActivity.ColorViewModel(5, 1, blue, false),
                TestableListActivity.ColorViewModel(2, 1, red, false)
        )))
    }

    inner class MockAdapter : ColorAdapter {

        val models: MutableList<TestableListActivity.ColorViewModel> = mutableListOf()
        lateinit override var presenter: ColorPresenter

        override fun notifyItemInserted(position: Int) {
            models.add(presenter.items[position])
        }
    }
}
