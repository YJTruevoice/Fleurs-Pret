package com.facile.immediate.electronique.fleurs.pret.loan.model

//产品状态: 0-可申请, 1-还款中, 2-逾期, 3-审核中, 4-拒绝, 5-放款失败,6-放款处理中
enum class ProState(val value: Int) {
    CAN_APPLY(0),
    REMBOURSEMENT(1),
    RETARDE(2),
    EN_EVALUATION(3),
    REJETEE(4),
    VERSEMENT_ECHOUE(5),
    VERSEMENT(6)
}