package com.github.ryanreymorris.orderescortbot.handler.button;

public enum ButtonEnum {

    EXIT("-Выйти-"),
    OPERATOR("-Оператор-"),
    GET_CONTACT("Получить контакты"),
    DELETE_TS_REQUEST("Закрыть заявку"),
    PURCHASE_AUTHOR("Выбрать автора (покупателя) заказов"),
    DELETE_AUTHOR_PURCHASES("Удалить заявку на заказ"),
    CONFIRM_PURCHASE("-Сделать заказ-"),
    UNDO("-Назад-"),
    REDO("-Вперед-"),
    TEC_SUPPORT("TS_");

    /**
     * Название кнопки
     */
    private final String buttonName;

    ButtonEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getName() {
        return buttonName;
    }

    public String getCode() {
        return this.name();
    }
}
