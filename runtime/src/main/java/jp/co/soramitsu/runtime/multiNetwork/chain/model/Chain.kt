package jp.co.soramitsu.runtime.multiNetwork.chain.model

import jp.co.soramitsu.common.data.network.BlockExplorerUrlBuilder
import java.util.Locale

typealias ChainId = String

const val polkadotChainId = "91b171bb158e2d3848fa23a9f1c25182fb8e20313b2c1eb49219da7a70ce90c3"
const val kusamaChainId = "b0a8d493285c2df73290dfb7e61f870f17b41801197a149ca93654499ea3dafe"

data class Chain(
    val id: ChainId,
    val name: String,
    val assets: List<Asset>,
    val nodes: List<Node>,
    val explorers: List<Explorer>,
    val externalApi: ExternalApi?,
    val icon: String,
    val addressPrefix: Int,
    val types: Types?,
    val isEthereumBased: Boolean,
    val isTestNet: Boolean,
    val hasCrowdloans: Boolean,
    val parentId: String?,
) {

    val assetsBySymbol = assets.associateBy(Asset::symbol)
    val assetsById = assets.associateBy(Asset::id)

    data class Types(
        val url: String,
        val overridesCommon: Boolean,
    )

    data class Asset(
        val id: String,
        val name: String,
        val iconUrl: String,
        val chainId: ChainId,
        val nativeChainId: ChainId?,
        val chainName: String?,
        val chainIcon: String?,
        val isTestNet: Boolean?,
        val priceId: String?,
        val precision: Int,
        val staking: StakingType,
        val priceProviders: List<String>?
    ) {

        enum class StakingType {
            UNSUPPORTED, RELAYCHAIN
        }

        val symbol: String
            get() = id.toUpperCase(Locale.ROOT)

        val isNative: Boolean
            get() = nativeChainId == null || nativeChainId == chainId

        val chainToSymbol = chainId to symbol
    }

    data class Node(
        val url: String,
        val name: String,
        val isActive: Boolean,
        val isDefault: Boolean
    )

    data class ExternalApi(
        val staking: Section?,
        val history: Section?,
        val crowdloans: Section?
    ) {
        data class Section(val type: Type, val url: String) {
            enum class Type {
                SUBQUERY, GITHUB, UNKNOWN
            }
        }
    }

    data class Explorer(val type: Type, val types: List<String>, val url: String) {
        enum class Type {
            POLKASCAN, SUBSCAN, UNKNOWN;

            val capitalizedName: String = name.lowercase().replaceFirstChar { it.titlecase() }
        }
    }
}

fun List<Chain.Explorer>.getSupportedExplorers(type: BlockExplorerUrlBuilder.Type, value: String) = mapNotNull {
    BlockExplorerUrlBuilder(it.url, it.types).build(type, value)?.let { url ->
        it.type to url
    }
}.toMap()

fun ChainId.isPolkadotOrKusama() = this in listOf(polkadotChainId, kusamaChainId)

enum class TypesUsage {
    BASE, OWN, BOTH,
}
