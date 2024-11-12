package kr.ac.cbnu.software.aws_control.data.repository

import aws.sdk.kotlin.services.ec2.Ec2Client
import aws.sdk.kotlin.services.ec2.model.DescribeRegionsRequest
import aws.sdk.kotlin.services.ec2.model.DescribeRegionsRequest.Companion.invoke
import aws.sdk.kotlin.services.ec2.model.Region
import javax.inject.Inject

internal class RegionRepository @Inject constructor(
    private val ec2Client: Ec2Client
) {
    suspend fun getRegions(): List<Region> {
        val request = DescribeRegionsRequest {}

        val response = ec2Client.describeRegions(request)
        return response.regions ?: emptyList()
    }
}