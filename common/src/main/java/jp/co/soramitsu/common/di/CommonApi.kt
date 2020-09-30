package jp.co.soramitsu.common.di

import android.content.Context
import com.google.gson.Gson
import jp.co.soramitsu.common.data.network.AppLinksProvider
import jp.co.soramitsu.common.data.network.NetworkApiCreator
import jp.co.soramitsu.common.data.network.rpc.RxWebSocket
import jp.co.soramitsu.common.data.storage.Preferences
import jp.co.soramitsu.common.data.storage.encrypt.EncryptedPreferences
import jp.co.soramitsu.common.resources.ClipboardManager
import jp.co.soramitsu.common.resources.ContextManager
import jp.co.soramitsu.common.resources.LanguagesHolder
import jp.co.soramitsu.common.resources.ResourceManager
import jp.co.soramitsu.common.vibration.DeviceVibrator
import jp.co.soramitsu.fearless_utils.bip39.Bip39
import jp.co.soramitsu.fearless_utils.encrypt.KeypairFactory
import jp.co.soramitsu.fearless_utils.icon.IconGenerator
import jp.co.soramitsu.fearless_utils.junction.JunctionDecoder
import jp.co.soramitsu.fearless_utils.ss58.SS58Encoder

interface CommonApi {

    fun context(): Context

    fun provideResourceManager(): ResourceManager

    fun provideNetworkApiCreator(): NetworkApiCreator

    fun provideAppLinksProvider(): AppLinksProvider

    fun providePreferences(): Preferences

    fun provideEncryptedPreferences(): EncryptedPreferences

    fun provideBip39(): Bip39

    fun provideKeypairFactory(): KeypairFactory

    fun provideSS58Encoder(): SS58Encoder

    fun provideJunctionDecoder(): JunctionDecoder

    fun provideIconGenerator(): IconGenerator

    fun provideClipboardManager(): ClipboardManager

    fun provideDeviceVibrator(): DeviceVibrator

    fun contextManager(): ContextManager

    fun languagesHolder(): LanguagesHolder

    fun provideRxWebSocket(): RxWebSocket

    fun provideJsonMapper(): Gson
}