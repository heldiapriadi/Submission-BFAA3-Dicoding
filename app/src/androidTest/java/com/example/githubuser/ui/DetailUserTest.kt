package com.example.githubuser.ui


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
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
class DetailUserTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun detailUserTest() {
        val searchAutoComplete = onView(
            allOf(
                withId(R.id.search_src_text),
                childAtPosition(
                    allOf(
                        withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete.perform(replaceText("sidiqpermana"), closeSoftKeyboard())

        val searchAutoComplete2 = onView(
            allOf(
                withId(R.id.search_src_text), withText("sidiqpermana"),
                childAtPosition(
                    allOf(
                        withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete2.perform(pressImeActionButton())

        Thread.sleep(2000)

        val editText = onView(
            allOf(
                withId(R.id.search_src_text), withText("sidiqpermana"),
                withParent(
                    allOf(
                        withId(R.id.search_plate),
                        withParent(withId(R.id.search_edit_frame))
                    )
                ),
                isDisplayed()
            )
        )
        editText.check(matches(withText("sidiqpermana")))

        val textView = onView(
            allOf(
                withId(R.id.tv_result), withText("Result: 2"),
                withParent(
                    allOf(
                        withId(R.id.relativeLayout),
                        withParent(withId(R.id.homeParent))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))

        val recyclerView = onView(
            allOf(
                withId(R.id.rvUsers),
                childAtPosition(
                    withId(R.id.relativeLayout),
                    3
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        Thread.sleep(2000)

        val textView2 = onView(
            allOf(
                withId(R.id.tvName), withText("Sidiq Permana"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(isDisplayed()))

        val textView3 = onView(
            allOf(
                withText("FOLLOWER"),
                withParent(
                    allOf(
                        withContentDescription("Follower"),
                        withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("FOLLOWER")))

        val recyclerView2 = onView(
            allOf(
                withParent(
                    allOf(
                        withId(R.id.view_pager),
                        withParent(withId(R.id.detailUserParent))
                    )
                ),
                isDisplayed()
            )
        )
        recyclerView2.check(matches(isDisplayed()))


        val tabView = onView(
            allOf(
                withContentDescription("Following"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tabs),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView.perform(click())

        Thread.sleep(2000)

        val textView4 = onView(
            allOf(
                withText("FOLLOWING"),
                withParent(
                    allOf(
                        withContentDescription("Following"),
                        withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("FOLLOWING")))

        val recyclerView3 = onView(
            allOf(
                withParent(
                    allOf(
                        withId(R.id.view_pager),
                        withParent(withId(R.id.detailUserParent))
                    )
                ),
                isDisplayed()
            )
        )
        recyclerView3.check(matches(isDisplayed()))
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
