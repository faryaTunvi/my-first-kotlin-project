package com.example.mymurmurapp.di

import android.content.Context
import androidx.room.Room
import com.example.mymurmurapp.data.local.MurmurDatabase
import com.example.mymurmurapp.data.remote.FirebaseService
import com.example.mymurmurapp.data.repository.AuthRepositoryImpl
import com.example.mymurmurapp.data.repository.MurmurRepositoryImpl
import com.example.mymurmurapp.data.repository.UserRepositoryImpl
import com.example.mymurmurapp.domain.repository.AuthRepository
import com.example.mymurmurapp.domain.repository.MurmurRepository
import com.example.mymurmurapp.domain.repository.UserRepository
import com.example.mymurmurapp.domain.usecase.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object AppContainer {
    private lateinit var database: MurmurDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseService: FirebaseService

    private lateinit var authRepository: AuthRepository
    private lateinit var murmurRepository: MurmurRepository
    private lateinit var userRepository: UserRepository

    fun init(context: Context) {
        // Initialize Room Database
        database = Room.databaseBuilder(
            context.applicationContext,
            MurmurDatabase::class.java,
            "murmur_database"
        ).build()

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseService = FirebaseService(FirebaseFirestore.getInstance())

        // Initialize Repositories
        authRepository = AuthRepositoryImpl(firebaseAuth, firebaseService)
        murmurRepository = MurmurRepositoryImpl(
            firebaseService,
            database.murmurDao(),
            authRepository
        )
        userRepository = UserRepositoryImpl(
            firebaseService,
            database.userDao(),
            authRepository
        )
    }

    // Repositories
    fun provideAuthRepository(): AuthRepository = authRepository
    fun provideMurmurRepository(): MurmurRepository = murmurRepository
    fun provideUserRepository(): UserRepository = userRepository

    // Use Cases
    fun provideGetTimelineUseCase(): GetTimelineUseCase =
        GetTimelineUseCase(murmurRepository)

    fun providePostMurmurUseCase(): PostMurmurUseCase =
        PostMurmurUseCase(murmurRepository)

    fun provideDeleteMurmurUseCase(): DeleteMurmurUseCase =
        DeleteMurmurUseCase(murmurRepository)

    fun provideLikeMurmurUseCase(): LikeMurmurUseCase =
        LikeMurmurUseCase(murmurRepository)

    fun provideGetUserMurmursUseCase(): GetUserMurmursUseCase =
        GetUserMurmursUseCase(murmurRepository)

    fun provideFollowUserUseCase(): FollowUserUseCase =
        FollowUserUseCase(userRepository)

    fun provideGetUserByIdUseCase(): GetUserByIdUseCase =
        GetUserByIdUseCase(userRepository)
}

