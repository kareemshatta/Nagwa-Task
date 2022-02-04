package com.kareem.domain.mapper

interface Mapper<in FROM, out TO> {
    fun map(from: FROM): TO
}