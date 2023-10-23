package com.facile.immediate.electronique.fleurs.pret.net.converter

import com.arthur.network.converter.JSONConverter
import com.facile.immediate.electronique.fleurs.pret.net.NetConstant

class CustomJsonConverter: JSONConverter() {
    override var successCode: Int  = NetConstant.SuccessCode.CODE_SUCCESS
    override var codeKey: String = NetConstant.ResponseRootKey.KEY_CODE
    override var msgKey: String = NetConstant.ResponseRootKey.KEY_MSG
    override var dataKey: String = NetConstant.ResponseRootKey.KEY_DATA
}