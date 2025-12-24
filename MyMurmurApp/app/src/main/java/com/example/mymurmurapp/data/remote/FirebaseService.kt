package com.example.mymurmurapp.data.remote

import com.example.mymurmurapp.data.remote.dto.MurmurDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class FirebaseService(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    companion object {
        private const val USERS_COLLECTION = "users"
        private const val MURMURS_COLLECTION = "murmurs"
        private const val FOLLOWS_COLLECTION = "follows"
        private const val LIKES_COLLECTION = "likes"
    }

    // Murmur operations
    suspend fun getTimeline(page: Int, pageSize: Int): List<MurmurDto> {
        return try {
            val snapshot = firestore.collection(MURMURS_COLLECTION)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(pageSize.toLong())
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(MurmurDto::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getMurmurById(murmurId: String): MurmurDto? {
        return try {
            firestore.collection(MURMURS_COLLECTION)
                .document(murmurId)
                .get()
                .await()
                .toObject(MurmurDto::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getUserMurmurs(userId: String, page: Int, pageSize: Int): List<MurmurDto> {
        return try {
            val snapshot = firestore.collection(MURMURS_COLLECTION)
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .limit(pageSize.toLong())
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(MurmurDto::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun postMurmur(murmur: MurmurDto): MurmurDto {
        val docRef = firestore.collection(MURMURS_COLLECTION).document()
        val murmurWithId = murmur.copy(id = docRef.id)
        docRef.set(murmurWithId).await()
        return murmurWithId
    }

    suspend fun deleteMurmur(murmurId: String) {
        firestore.collection(MURMURS_COLLECTION)
            .document(murmurId)
            .delete()
            .await()
    }

    // Like operations
    suspend fun likeMurmur(userId: String, murmurId: String) {
        val likeId = "${userId}_${murmurId}"
        val like = hashMapOf(
            "id" to likeId,
            "userId" to userId,
            "murmurId" to murmurId,
            "createdAt" to System.currentTimeMillis()
        )
        firestore.collection(LIKES_COLLECTION)
            .document(likeId)
            .set(like)
            .await()

        // Update likes count
        val murmurRef = firestore.collection(MURMURS_COLLECTION).document(murmurId)
        firestore.runTransaction { transaction ->
            val murmur = transaction.get(murmurRef).toObject(MurmurDto::class.java)
            murmur?.let {
                transaction.update(murmurRef, "likesCount", it.likesCount + 1)
            }
        }.await()
    }

    suspend fun unlikeMurmur(userId: String, murmurId: String) {
        val likeId = "${userId}_${murmurId}"
        firestore.collection(LIKES_COLLECTION)
            .document(likeId)
            .delete()
            .await()

        // Update likes count
        val murmurRef = firestore.collection(MURMURS_COLLECTION).document(murmurId)
        firestore.runTransaction { transaction ->
            val murmur = transaction.get(murmurRef).toObject(MurmurDto::class.java)
            murmur?.let {
                transaction.update(murmurRef, "likesCount", maxOf(0, it.likesCount - 1))
            }
        }.await()
    }

    suspend fun isLikedByUser(userId: String, murmurId: String): Boolean {
        return try {
            val likeId = "${userId}_${murmurId}"
            val doc = firestore.collection(LIKES_COLLECTION)
                .document(likeId)
                .get()
                .await()
            doc.exists()
        } catch (e: Exception) {
            false
        }
    }

    // User operations
    suspend fun getUserById(userId: String): com.example.mymurmurapp.data.remote.dto.UserDto? {
        return try {
            firestore.collection(USERS_COLLECTION)
                .document(userId)
                .get()
                .await()
                .toObject(com.example.mymurmurapp.data.remote.dto.UserDto::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createUser(user: com.example.mymurmurapp.data.remote.dto.UserDto) {
        firestore.collection(USERS_COLLECTION)
            .document(user.id)
            .set(user)
            .await()
    }

    suspend fun updateUser(user: com.example.mymurmurapp.data.remote.dto.UserDto) {
        firestore.collection(USERS_COLLECTION)
            .document(user.id)
            .set(user)
            .await()
    }

    // Follow operations
    suspend fun followUser(followerId: String, followingId: String) {
        val followId = "${followerId}_${followingId}"
        val follow = hashMapOf(
            "followerId" to followerId,
            "followingId" to followingId,
            "createdAt" to System.currentTimeMillis()
        )
        firestore.collection(FOLLOWS_COLLECTION)
            .document(followId)
            .set(follow)
            .await()

        // Update counts
        updateFollowCounts(followerId, followingId, increment = true)
    }

    suspend fun unfollowUser(followerId: String, followingId: String) {
        val followId = "${followerId}_${followingId}"
        firestore.collection(FOLLOWS_COLLECTION)
            .document(followId)
            .delete()
            .await()

        // Update counts
        updateFollowCounts(followerId, followingId, increment = false)
    }

    private suspend fun updateFollowCounts(followerId: String, followingId: String, increment: Boolean) {
        val delta = if (increment) 1 else -1

        // Update follower's following count
        val followerRef = firestore.collection(USERS_COLLECTION).document(followerId)
        firestore.runTransaction { transaction ->
            val follower = transaction.get(followerRef).toObject(com.example.mymurmurapp.data.remote.dto.UserDto::class.java)
            follower?.let {
                transaction.update(followerRef, "followingCount", maxOf(0, it.followingCount + delta))
            }
        }.await()

        // Update following's followers count
        val followingRef = firestore.collection(USERS_COLLECTION).document(followingId)
        firestore.runTransaction { transaction ->
            val following = transaction.get(followingRef).toObject(com.example.mymurmurapp.data.remote.dto.UserDto::class.java)
            following?.let {
                transaction.update(followingRef, "followersCount", maxOf(0, it.followersCount + delta))
            }
        }.await()
    }

    suspend fun isFollowing(followerId: String, followingId: String): Boolean {
        return try {
            val followId = "${followerId}_${followingId}"
            val doc = firestore.collection(FOLLOWS_COLLECTION)
                .document(followId)
                .get()
                .await()
            doc.exists()
        } catch (e: Exception) {
            false
        }
    }
}

