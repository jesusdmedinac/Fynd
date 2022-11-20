package com.jesusdmedinac.fynd.data.remote.mapper

import com.google.common.hash.HashFunction
import javax.inject.Inject
import javax.inject.Named

class PasswordStringHasher @Inject constructor(
    @Named("sha256")
    private val hashFunction: HashFunction,
) {
    operator fun invoke(input: String): String = hashFunction
        .newHasher()
        .putString(input, Charsets.UTF_8)
        .hash()
        .toString()
}