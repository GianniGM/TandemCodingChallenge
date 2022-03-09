package com.giannig.tandemlite.paging

import com.giannig.tandemlite.api.dto.TandemUser
import com.giannig.tandemlite.paging.TandemPagingSource.Companion.joinLikedUsersWith
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class TandemPagingSourceTest{

    @Test
    fun `join empty list from api and not empty list from db`() {
        //Given
        val usersFromDB = usersFromDb

        //When
        val usersFromApi = emptyList<TandemUser>()

        //Then
        assertEquals(usersFromDB.joinLikedUsersWith(usersFromApi), usersFromDB)
    }

    @Test
    fun `join empty list from db and not empty list from api`() {
        //Given
        val usersFromDB = emptyList<TandemUser>()

        //When
        val usersFromApi = usersFromApi

        //Then
        assertEquals(usersFromDB.joinLikedUsersWith(usersFromApi), usersFromApi)
    }

    @Test
    fun `join liked users from db, with new users coming from apis`() {
        //Given
        val usersFromDB = usersFromDb

        //When
        val usersFromApi = usersFromApi

        //Then
        assertEquals(usersFromDB.joinLikedUsersWith(usersFromApi), expectedResult)
    }

    companion object {
        val usersFromDb = listOf(
            TandemUser(
                id = 1,
                firstName = "James",
                learns = listOf("DE, EN"),
                natives = listOf("IT"),
                pictureUrl = "www.profilepic.com",
                referenceCnt = 0,
                liked = false,
                topic = "I am james"
            ),
            TandemUser(
                id = 3,
                firstName = "Alice",
                learns = listOf("DE, EN"),
                natives = listOf("IT"),
                pictureUrl = "www.profilepic.com",
                referenceCnt = 0,
                liked = true,
                topic = "I am not james"
            ),
            TandemUser(
                id = 4,
                firstName = "Roberta",
                learns = listOf("DE, EN"),
                natives = listOf("IT"),
                pictureUrl = "www.profilepic.com",
                referenceCnt = 0,
                liked = true,
                topic = "I am Robby"
            )

        )

        val usersFromApi = listOf(
            TandemUser(
                id = 1,
                firstName = "James",
                learns = listOf("DE, EN"),
                natives = listOf("IT"),
                pictureUrl = "www.profilepic.com",
                referenceCnt = 0,
                topic = "I am james"
            ),
            TandemUser(
                id = 2,
                firstName = "Anna",
                learns = listOf("DE, EN"),
                natives = listOf("IT"),
                pictureUrl = "www.profilepic.com",
                referenceCnt = 0,
                topic = "I am Anna"
            ),
            TandemUser(
                id = 3,
                firstName = "Alice",
                learns = listOf("DE, EN"),
                natives = listOf("IT"),
                pictureUrl = "www.profilepic.com",
                referenceCnt = 0,
                topic = "I am not james"
            ),
            TandemUser(
                id = 4,
                firstName = "Roberta",
                learns = listOf("DE, EN"),
                natives = listOf("IT"),
                pictureUrl = "www.profilepic.com",
                referenceCnt = 0,
                topic = "I am Robby"
            )
        )


        val expectedResult = listOf(
            TandemUser(
                id = 1,
                firstName = "James",
                learns = listOf("DE, EN"),
                natives = listOf("IT"),
                pictureUrl = "www.profilepic.com",
                referenceCnt = 0,
                topic = "I am james",
                liked = false,
            ),
            TandemUser(
                id = 2,
                firstName = "Anna",
                learns = listOf("DE, EN"),
                natives = listOf("IT"),
                pictureUrl = "www.profilepic.com",
                referenceCnt = 0,
                liked = false,
                topic = "I am Anna"
            ),
            TandemUser(
                id = 3,
                firstName = "Alice",
                learns = listOf("DE, EN"),
                natives = listOf("IT"),
                pictureUrl = "www.profilepic.com",
                referenceCnt = 0,
                liked = true,
                topic = "I am not james"
            ),
            TandemUser(
                id = 4,
                firstName = "Roberta",
                learns = listOf("DE, EN"),
                natives = listOf("IT"),
                pictureUrl = "www.profilepic.com",
                referenceCnt = 0,
                liked = true,
                topic = "I am Robby"
            )
        )

    }

}