package jp.co.soramitsu.feature_crowdloan_impl.data.network.api.acala

import java.math.BigInteger

class AcalaContributeRequest(
    val address: String,
    val amount: BigInteger,
    val signature: String,
    val referral: String?,
    val email: String?,
    val receiveEmail: Boolean?,
)
