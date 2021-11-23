package jp.co.soramitsu.feature_account_api.domain.interfaces

import jp.co.soramitsu.core.model.*
import jp.co.soramitsu.fearless_utils.runtime.AccountId
import jp.co.soramitsu.feature_account_api.domain.model.*
import kotlinx.coroutines.flow.Flow

class AccountAlreadyExistsException : Exception()

interface AccountRepository {

    fun getEncryptionTypes(): List<CryptoType>

    suspend fun getNode(nodeId: Int): Node

    suspend fun getNetworks(): List<Network>

    suspend fun getSelectedNodeOrDefault(): Node

    suspend fun selectNode(node: Node)

    suspend fun getDefaultNode(networkType: Node.NetworkType): Node

    suspend fun selectAccount(metaAccountId: Long, newNode: Node? = null)

    fun selectedAccountFlow(): Flow<Account>

    suspend fun getSelectedAccount(): Account

    suspend fun getSelectedAccount(chainId: String): Account
    suspend fun getSelectedMetaAccount(): MetaAccount
    suspend fun getMetaAccount(metaId: Long): MetaAccount
    fun selectedMetaAccountFlow(): Flow<MetaAccount>

    suspend fun findMetaAccount(accountId: ByteArray): MetaAccount?

    suspend fun allMetaAccounts(): List<MetaAccount>

    fun lightMetaAccountsFlow(): Flow<List<LightMetaAccount>>
    suspend fun selectMetaAccount(metaId: Long)

    suspend fun updateMetaAccountName(metaId: Long, newName: String)

    suspend fun getPreferredCryptoType(): CryptoType

    suspend fun isAccountSelected(): Boolean

    suspend fun createAccount(
        accountName: String,
        mnemonic: String,
        encryptionType: CryptoType,
        derivationPath: String
    )

    suspend fun deleteAccount(metaId: Long)

    suspend fun getAccounts(): List<Account>

    suspend fun getAccount(address: String): Account

    suspend fun getAccountOrNull(address: String): Account?

    suspend fun getMyAccounts(query: String, chainId: String): Set<Account>

    suspend fun importFromMnemonic(
        keyString: String,
        username: String,
        derivationPath: String,
        selectedEncryptionType: CryptoType
    )

    suspend fun importFromSeed(
        seed: String,
        username: String,
        derivationPath: String,
        selectedEncryptionType: CryptoType
    )

    suspend fun importFromJson(
        json: String,
        password: String,
        name: String
    )

    suspend fun isCodeSet(): Boolean

    suspend fun savePinCode(code: String)

    suspend fun getPinCode(): String?

    suspend fun generateMnemonic(): List<String>

    suspend fun isBiometricEnabled(): Boolean

    suspend fun setBiometricOn()

    suspend fun setBiometricOff()

    fun nodesFlow(): Flow<List<Node>>

    suspend fun updateAccountsOrdering(accountOrdering: List<MetaAccountOrdering>)

    suspend fun processAccountJson(json: String): ImportJsonData

    fun getLanguages(): List<Language>

    suspend fun selectedLanguage(): Language

    suspend fun changeLanguage(language: Language)

    suspend fun getSecuritySource(accountAddress: String): SecuritySource

    suspend fun addNode(nodeName: String, nodeHost: String, networkType: Node.NetworkType)

    suspend fun updateNode(nodeId: Int, newName: String, newHost: String, networkType: Node.NetworkType)

    suspend fun checkNodeExists(nodeHost: String): Boolean

    /**
     * @throws FearlessException
     */
    suspend fun getNetworkName(nodeHost: String): String

    suspend fun getAccountsByNetworkType(networkType: Node.NetworkType): List<Account>

    suspend fun deleteNode(nodeId: Int)

    fun createQrAccountContent(account: Account): String

    suspend fun generateRestoreJson(account: Account, password: String): String

    suspend fun isAccountExists(accountId: AccountId): Boolean
}
