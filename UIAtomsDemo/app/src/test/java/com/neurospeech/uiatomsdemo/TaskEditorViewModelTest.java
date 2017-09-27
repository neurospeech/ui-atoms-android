package com.neurospeech.uiatomsdemo;

import com.neurospeech.uiatomsdemo.viewmodels.TaskEditorViewModel;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TaskEditorViewModelTest {
    @Test
    public void loginCommandTest() throws Exception {
        TaskEditorViewModel viewModel = new TaskEditorViewModel();

        assertNull(viewModel.error.get());

        viewModel.addCommand.execute(null);

        assertTrue(viewModel.error.get().equals("Task cannot be empty"));

        viewModel.newTask.get().label = "Test";
        viewModel.addCommand.execute(null);

        assertNull(viewModel.error.get());
    }
}