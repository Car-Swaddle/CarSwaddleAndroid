package com.carswaddle.carswaddleandroid.ui.activities.schedule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carswaddle.carswaddleandroid.R
import com.carswaddle.carswaddleandroid.ui.view.ProgressBubble

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProgressFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProgressFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var bubble1: ProgressBubble
    private lateinit var bubble2: ProgressBubble
    private lateinit var bubble3: ProgressBubble
    private var _stepNumber: Int = 0

    var stepNumber: Int
        get() = _stepNumber
        set(value) {
            _stepNumber = value
            bubble1.state = if (value == 1) ProgressBubble.ProgressState.Active else ProgressBubble.ProgressState.Complete
            bubble2.state = when (value) {
                1 -> ProgressBubble.ProgressState.Inactive
                2 -> ProgressBubble.ProgressState.Active
                else -> ProgressBubble.ProgressState.Complete
            }
            bubble3.state = if (value == 3) ProgressBubble.ProgressState.Active else ProgressBubble.ProgressState.Inactive
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bubble1 = view.findViewById(R.id.bubble1)
        bubble1.state = ProgressBubble.ProgressState.Active
        bubble1.stepNumber = 1
        bubble2 = view.findViewById(R.id.bubble2)
        bubble2.state = ProgressBubble.ProgressState.Inactive
        bubble2.stepNumber = 2
        bubble3 = view.findViewById(R.id.bubble3)
        bubble3.state = ProgressBubble.ProgressState.Inactive
        bubble3.stepNumber = 3
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProgressFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProgressFragment()
                .apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}