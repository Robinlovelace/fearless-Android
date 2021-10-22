package jp.co.soramitsu.feature_crowdloan_impl.data.network.api.moonbeam

import com.google.gson.annotations.SerializedName

class RemarkVerifyRequest(
    val address: String,
    @SerializedName("block-hash")
    val blockHash: String,
    @SerializedName("extrinsic-hash")
    val extrinsicHash: String
)