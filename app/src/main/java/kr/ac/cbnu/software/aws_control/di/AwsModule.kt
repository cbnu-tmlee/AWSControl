package kr.ac.cbnu.software.aws_control.di

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.ec2.Ec2Client
import aws.sdk.kotlin.services.ssm.SsmClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AwsModule {
    @Provides
    @Singleton
    fun provideEc2Client(
        staticCredentialsProvider: StaticCredentialsProvider
    ) = Ec2Client {
        region = "ap-northeast-2"
        credentialsProvider = staticCredentialsProvider
    }

    @Provides
    @Singleton
    fun provideSsmClient(
        staticCredentialsProvider: StaticCredentialsProvider
    ) = SsmClient {
        region = "ap-northeast-2"
        credentialsProvider = staticCredentialsProvider
    }
}