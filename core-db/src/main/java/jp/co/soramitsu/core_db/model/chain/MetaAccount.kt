package jp.co.soramitsu.core_db.model.chain

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import jp.co.soramitsu.core.model.CryptoType

@Entity(
    tableName = MetaAccountLocal.TABLE_NAME,
    indices = [
        Index(value = ["substrateAccountId"]),
        Index(value = ["ethereumAddress"])
    ]
)
class MetaAccountLocal(
    val substratePublicKey: ByteArray,
    val substrateCryptoType: CryptoType,
    val substrateAccountId: ByteArray,
    val ethereumPublicKey: ByteArray?,
    val ethereumAddress: String?,
    val name: String,
    val isSelected: Boolean,
) {

    companion object Table {
        const val TABLE_NAME = "meta_accounts"

        object Column {
            const val SUBSTRATE_PUBKEY = "substratePublicKey"
            const val SUBSTRATE_CRYPTO_TYPE = "substrateCryptoType"
            const val SUBSTRATE_ACCOUNT_ID = "substrateAccountId"

            const val ETHEREUM_PUBKEY = "ethereumPublicKey"
            const val ETHEREUM_ADDRESS = "ethereumAddress"

            const val NAME = "name"
            const val IS_SELECTED = "isSelected"
        }
    }

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}

@Entity(
    tableName = "chain_accounts",
    foreignKeys = [
        ForeignKey(
            parentColumns = ["id"],
            childColumns = ["chainId"],
            entity = ChainLocal::class
        ),
        ForeignKey(
            parentColumns = ["id"],
            childColumns = ["metaId"],
            entity = MetaAccountLocal::class,
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["metaId", "chainId"], unique = true)
    ],
    primaryKeys = ["metaId", "chainId"]
)
class ChainAccountLocal(
    val metaId: Long,
    val chainId: String,
    val publicKey: ByteArray,
    val cryptoType: CryptoType,
)

class JoinedMetaAccountInfo(
    @Embedded
    val metaAccount: MetaAccountLocal,

    @Relation(parentColumn = "id", entityColumn = "metaId", entity = ChainAccountLocal::class)
    val chainAccounts: List<ChainAccountLocal>,
)
