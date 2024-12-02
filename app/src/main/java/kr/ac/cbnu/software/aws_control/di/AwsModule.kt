package kr.ac.cbnu.software.aws_control.di

import aws.sdk.kotlin.runtime.auth.credentials.StaticCredentialsProvider
import aws.sdk.kotlin.services.cloudwatch.CloudWatchClient
import aws.sdk.kotlin.services.ec2.Ec2Client
import aws.sdk.kotlin.services.ssm.SsmClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.ac.cbnu.software.aws_control.BuildConfig
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

    @Provides
    @Singleton
    fun provideCloudWatchClient(
        staticCredentialsProvider: StaticCredentialsProvider
    ) = CloudWatchClient {
        region = "ap-northeast-2"
        credentialsProvider = staticCredentialsProvider
    }

    @Provides
    @Singleton
    fun provideProfileCredentialsProvider() = StaticCredentialsProvider {
        accessKeyId = BuildConfig.ACCESS_KEY_ID
        secretAccessKey = BuildConfig.SECRET_ACCESS_KEY
    }
}