package lt.dariusl.testablelist

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
        val presenter = ColorPresenter(adapter, emptyList())
        assertThat(adapter.presenter, `is`(presenter))
        assertThat(adapter.models, `is`(mutableListOf()))
    }

    @Test
    fun testInsertSingleInitialValue() {
        ColorPresenter(adapter, listOf(RowModel(1, red)))
        assertThat(adapter.models, `is`(listOf(TestableListActivity.ColorViewModel(1, 1, red))))
    }

    @Test
    fun testInitialValuesAreSorted() {
        ColorPresenter(adapter, listOf(RowModel(2, red), RowModel(5, blue)))
        assertThat(adapter.models, `is`(listOf(
                TestableListActivity.ColorViewModel(5, 1, blue),
                TestableListActivity.ColorViewModel(2, 1, red)
        )))
    }

    @Test
    fun testInitialGroupSizesAreSet() {
        ColorPresenter(adapter, listOf(
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
        val presenter = ColorPresenter(adapter, listOf(
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
        val presenter = ColorPresenter(adapter, listOf(
                RowModel(10, red)
        ))
        presenter.insert(RowModel(8, red))
        assertThat(adapter.models, hasItems(
                size(`is`(2)),
                size(`is`(2))
        ))
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
