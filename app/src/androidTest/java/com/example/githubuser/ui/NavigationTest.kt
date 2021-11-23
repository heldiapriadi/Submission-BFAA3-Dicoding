package com.example.githubuser.ui


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.example.githubuser.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun navigationTest() {
        val textView = onView(
            allOf(
                withId(R.id.tv_search), withText("Search"),
                withParent(
                    allOf(
                        withId(R.id.relativeLayout),
                        withParent(withId(R.id.homeParent))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Search")))


        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.navigation_favorite), withContentDescription("Favorite"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val textView3 = onView(
            allOf(
                withId(R.id.tv_favorite), withText("Favorite"),
                withParent(withParent(withId(R.id.nav_host_fragment_activity_main))),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Favorite")))


        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.navigation_home), withContentDescription("Home"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())

        val textView5 = onView(
            allOf(
                withId(R.id.tv_search), withText("Search"),
                withParent(
                    allOf(
                        withId(R.id.relativeLayout),
                        withParent(withId(R.id.homeParent))
                    )
                ),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("Search")))

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.btnSettings),
                childAtPosition(
                    allOf(
                        withId(R.id.relativeLayout),
                        childAtPosition(
                            withId(R.id.homeParent),
                            0
                        )
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())

        val textView6 = onView(
            allOf(
                withId(android.R.id.title), withText("Dark mode"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView6.check(matches(withText("Dark mode")))

        val appCompatImageButton2 = onView(
            allOf(
                withContentDescription("Navigate up"),
                childAtPosition(
                    allOf(
                        withId(R.id.action_bar),
                        childAtPosition(
                            withId(R.id.action_bar_container),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
