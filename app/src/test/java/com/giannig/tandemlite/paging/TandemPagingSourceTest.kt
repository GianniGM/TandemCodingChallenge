package com.giannig.tandemlite.paging

import com.giannig.tandemlite.api.dto.TandemUser
import com.giannig.tandemlite.paging.TandemPagingSource.Companion.putLikedUsersInto
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

@RunWith(BlockJUnit4ClassRunner::class)
class TandemPagingSourceTest{

    @Test
    fun `join empty list from api and not empty list from db, empty list returned`() {
        //Given
        val usersFromDB = usersFromDb

        //When
        val usersFromApi = emptyList<TandemUser>()

        //Then
        assertEquals(usersFromDB.putLikedUsersInto(usersFromApi), emptyList<TandemUser>())
    }

    @Test
    fun `join empty list from db and not empty list from api`() {
        //Given
        val usersFromDB = emptyList<TandemUser>()

        //When
        val usersFromApi = usersFromApi

        //Then
        assertEquals(usersFromDB.putLikedUsersInto(usersFromApi), usersFromApi)
    }

    @Test
    fun `join liked users from db, with new users coming from apis`() {
        //Given
        val usersFromDB = usersFromDb

        //When
        val usersFromApi = usersFromApi

        //Then
        assertEquals(usersFromDB.putLikedUsersInto(usersFromApi), expectedResult)
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