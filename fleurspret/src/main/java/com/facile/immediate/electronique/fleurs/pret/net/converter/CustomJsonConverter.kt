package com.facile.immediate.electronique.fleurs.pret.net.converter

import com.arthur.network.converter.JSONConverter
import com.facile.immediate.electronique.fleurs.pret.net.NetBizConstant

class CustomJsonConverter: JSONConverter() {
    override var successCode: Int  = NetBizConstant.SuccessCode.CODE_SUCCESS
    override var codeKey: String = NetBizConstant.ResponseRootKey.KEY_CODE
    override var msgKey: String = NetBizConstant.ResponseRootKey.KEY_MSG
    override var dataKey: String = NetBizConstant.ResponseRootKey.KEY_DATA
}