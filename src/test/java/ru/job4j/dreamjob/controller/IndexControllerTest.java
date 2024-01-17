package ru.job4j.dreamjob.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;

import static org.assertj.core.api.Assertions.assertThat;

class IndexControllerTest {
    @Test
    public void whenIndexControllerGetIndexThanSuccessfully() {
        IndexController indexController = new IndexController();
        String expected = "index";

        var model = new ConcurrentModel();
        var view = indexController.getIndex(model);

        assertThat(view).isEqualTo(expected);
    }
}