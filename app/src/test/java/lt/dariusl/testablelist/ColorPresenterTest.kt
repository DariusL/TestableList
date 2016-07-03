package lt.dariusl.testablelist

import android.os.Bundle
import org.hamcrest.FeatureMatcher
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricGradleTestRunner

@RunWith(RobolectricGradleTestRunner::class)
class ColorPresenterTest {

    val red = 0x00FF0000
    val green = 0x0000FF00
    val blue = 0x000000FF

    val adapter = MockAdapter()

    @Test
    fun testInitialize() {
        val presenter = instantiate(emptyList())
        assertThat(adapter.presenter, `is`(presenter))
        assertThat(adapter.models, `is`(mutableListOf()))
    }

    @Test
    fun testInsertSingleInitialValue() {
        instantiate(listOf(RowModel(1, red)))
        assertThat(adapter.models, `is`(listOf(TestableListActivity.ColorViewModel(1, 1, red))))
    }

    @Test
    fun testInitialValuesAreSorted() {
        instantiate(listOf(RowModel(2, red), RowModel(5, blue)))
        assertThat(adapter.models, `is`(listOf(
                TestableListActivity.ColorViewModel(5, 1, blue),
                TestableListActivity.ColorViewModel(2, 1, red)
        )))
    }

    @Test
    fun testInitialGroupSizesAreSet() {
        instantiate(listOf(
                RowModel(10, green),
                RowModel(5, red),
                RowModel(2, red),
                RowModel(1, blue)))
        assertThat(adapter.models, hasItems(
                allOf(color(`is`(green)), size(`is`(1))),
                allOf(color(`is`(red)), size(`is`(2))),
                allOf(color(`is`(red)), size(`is`(2))),
                allOf(color(`is`(blue)), size(`is`(1)))
        ))
    }

    @Test
    fun testInsertKeepsOrder() {
        val presenter = instantiate(listOf(
                RowModel(10, green),
                RowModel(5, blue)
        ))
        presenter.insert(RowModel(8, red))
        assertThat(adapter.models, hasItems(
                color(`is`(green)),
                color(`is`(red)),
                color(`is`(blue))
        ))
    }

    @Test
    fun testInsertUpdatesGroupSizes() {
        val presenter = instantiate(listOf(
                RowModel(10, red)
        ))
        presenter.insert(RowModel(8, red))
        assertThat(adapter.models, hasItems(
                size(`is`(2)),
                size(`is`(2))
        ))
    }

    @Test
    fun testSaveState() {
        var presenter = instantiate(listOf(RowModel(10, red), RowModel(5, blue)))
        val state = Bundle()
        val items = presenter.items.toList()
        presenter.saveState(state)
        presenter = ColorPresenter(adapter, emptyList(), state)
        assertThat(presenter.items, `is`(items))
    }

    fun priority(matcher: Matcher<Int>) : Matcher<TestableListActivity.ColorViewModel> {
        return propertyMatcher(matcher, {it.priority}, "priority")
    }

    fun color(matcher: Matcher<Int>): Matcher<TestableListActivity.ColorViewModel> {
        return propertyMatcher(matcher, { it.color}, "color")
    }

    fun size(matcher: Matcher<Int>): Matcher<TestableListActivity.ColorViewModel> {
        return propertyMatcher(matcher, {it.size}, "size")
    }

    fun <T> propertyMatcher(matcher: Matcher<T>, extractor: (TestableListActivity.ColorViewModel) -> T, name: String): Matcher<TestableListActivity.ColorViewModel> {
        return object : FeatureMatcher<TestableListActivity.ColorViewModel, T> (matcher, name, name) {
            override fun featureValueOf(actual: TestableListActivity.ColorViewModel?): T {
                return extractor(actual!!)
            }
        }
    }

    fun instantiate(values: List<RowModel>) : ColorPresenter = instantiate(values, null)

    fun instantiate(values: List<RowModel>, state: Bundle?) = ColorPresenter(adapter, values, state)

    class MockAdapter : ColorAdapter {

        val models: MutableList<TestableListActivity.ColorViewModel> = mutableListOf()
        lateinit override var presenter: ColorPresenter

        override fun notifyItemInserted(position: Int) {
            models.add(presenter.items[position])
        }

        override fun notifyDataSetChanged() {
            models.clear()
            models.addAll(presenter.items)
        }
    }
}
