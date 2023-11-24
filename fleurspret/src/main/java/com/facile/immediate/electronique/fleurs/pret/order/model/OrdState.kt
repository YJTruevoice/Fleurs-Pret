package com.facile.immediate.electronique.fleurs.pret.order.model

//订单状态: 0-审核中, 1-拒绝, 2-订单完成, 3-逾期, 4-还款中, 5-放款失败,6-放款处理中
enum class OrdState(val value: Int) {
    EN_EVALUATION(0),
    REJETEE(1),
    QUITTE(2),
    RETARDE(3),
    REMBOURSEMENT(4),
    VERSEMENT_ECHOUE(5),
    VERSEMENT(6)
}