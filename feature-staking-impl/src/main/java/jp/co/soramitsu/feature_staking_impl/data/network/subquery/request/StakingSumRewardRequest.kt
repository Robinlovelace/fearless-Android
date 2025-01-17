package jp.co.soramitsu.feature_staking_impl.data.network.subquery.request

class StakingSumRewardRequest(accountAddress: String) {
    val query = """
    query {
        historyElements(
        filter: {
            reward: { notEqualTo: "null"},
            address: { equalTo: "$accountAddress"}  }
        ) {
            nodes {
                  reward
            }
        }
    }
    """.trimIndent()
}
