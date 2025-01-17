package jp.co.soramitsu.feature_wallet_api.domain.model

import jp.co.soramitsu.runtime.multiNetwork.chain.model.Chain
import java.math.BigInteger

class Asset(
    val metaId: Long?,
    val token: Token,
    val freeInPlanks: BigInteger,
    val reservedInPlanks: BigInteger,
    val miscFrozenInPlanks: BigInteger,
    val feeFrozenInPlanks: BigInteger,
    val bondedInPlanks: BigInteger,
    val redeemableInPlanks: BigInteger,
    val unbondingInPlanks: BigInteger
) {
    companion object {
        fun createEmpty(chainAsset: Chain.Asset, metaId: Long? = null) = Asset(
            metaId = metaId,
            Token(configuration = chainAsset, dollarRate = null, recentRateChange = null),
            freeInPlanks = BigInteger.ZERO,
            reservedInPlanks = BigInteger.ZERO,
            miscFrozenInPlanks = BigInteger.ZERO,
            feeFrozenInPlanks = BigInteger.ZERO,
            bondedInPlanks = BigInteger.ZERO,
            redeemableInPlanks = BigInteger.ZERO,
            unbondingInPlanks = BigInteger.ZERO
        )
    }

    val free = token.amountFromPlanks(freeInPlanks)
    val reserved = token.amountFromPlanks(reservedInPlanks)
    val miscFrozen = token.amountFromPlanks(miscFrozenInPlanks)
    val feeFrozen = token.amountFromPlanks(feeFrozenInPlanks)

    val locked = miscFrozen.max(feeFrozen)
    val frozen = locked + reserved

    val total = token.amountFromPlanks(calculateTotalBalance(freeInPlanks, reservedInPlanks))

    val transferable = free - locked

    val bonded = token.amountFromPlanks(bondedInPlanks)
    val redeemable = token.amountFromPlanks(redeemableInPlanks)
    val unbonding = token.amountFromPlanks(unbondingInPlanks)

    val dollarAmount = token.dollarRate?.multiply(total)
}

fun calculateTotalBalance(
    freeInPlanks: BigInteger,
    reservedInPlanks: BigInteger
) = freeInPlanks + reservedInPlanks
