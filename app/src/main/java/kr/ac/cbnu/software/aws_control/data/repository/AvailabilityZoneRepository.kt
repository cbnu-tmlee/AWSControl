package kr.ac.cbnu.software.aws_control.data.repository

import aws.sdk.kotlin.services.ec2.Ec2Client
import aws.sdk.kotlin.services.ec2.model.AvailabilityZone
import aws.sdk.kotlin.services.ec2.model.DescribeAvailabilityZonesRequest
import aws.sdk.kotlin.services.ec2.model.DescribeAvailabilityZonesRequest.Companion.invoke
import javax.inject.Inject

internal class AvailabilityZoneRepository @Inject constructor(
    private val ec2Client: Ec2Client
) {
    suspend fun getAvailabilityZones(): List<AvailabilityZone> {
        val request = DescribeAvailabilityZonesRequest {}

        val response = ec2Client.describeAvailabilityZones(request)
        return response.availabilityZones ?: emptyList()
    }
}