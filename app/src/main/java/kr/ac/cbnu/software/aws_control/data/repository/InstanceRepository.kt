package kr.ac.cbnu.software.aws_control.data.repository

import aws.sdk.kotlin.services.ec2.Ec2Client
import aws.sdk.kotlin.services.ec2.model.DescribeInstancesRequest
import aws.sdk.kotlin.services.ec2.model.DescribeInstancesRequest.Companion.invoke
import aws.sdk.kotlin.services.ec2.model.InstanceType
import aws.sdk.kotlin.services.ec2.model.RebootInstancesRequest
import aws.sdk.kotlin.services.ec2.model.RebootInstancesRequest.Companion.invoke
import aws.sdk.kotlin.services.ec2.model.Reservation
import aws.sdk.kotlin.services.ec2.model.ResourceType
import aws.sdk.kotlin.services.ec2.model.RunInstancesRequest
import aws.sdk.kotlin.services.ec2.model.StartInstancesRequest
import aws.sdk.kotlin.services.ec2.model.StartInstancesRequest.Companion.invoke
import aws.sdk.kotlin.services.ec2.model.StopInstancesRequest
import aws.sdk.kotlin.services.ec2.model.StopInstancesRequest.Companion.invoke
import aws.sdk.kotlin.services.ec2.model.Tag
import aws.sdk.kotlin.services.ec2.model.TagSpecification
import aws.sdk.kotlin.services.ec2.model.TerminateInstancesRequest
import javax.inject.Inject

internal class InstanceRepository @Inject constructor(
    private val ec2Client: Ec2Client
) {
    suspend fun runInstance(
        name: String,
        imageId: String,
        instanceType: InstanceType
    ) {
        val tagSpec = TagSpecification {
            resourceType = ResourceType.Instance
            tags = listOf(
                Tag {
                    key = "Name"
                    value = name
                }
            )
        }

        val request = RunInstancesRequest {
            this.imageId = imageId
            this.instanceType = instanceType
            minCount = 1
            maxCount = 1
            tagSpecifications = listOf(tagSpec)
        }

        ec2Client.runInstances(request)
    }

    suspend fun getInstances(): List<Reservation> {
        val request = DescribeInstancesRequest {}
        val response = ec2Client.describeInstances(request)

        return response.reservations ?: emptyList()
    }

    suspend fun startInstances(instanceIds: List<String>) {
        val request = StartInstancesRequest {
            this.instanceIds = instanceIds
        }

        ec2Client.startInstances(request)
    }

    suspend fun stopInstances(instanceIds: List<String>) {
        val request = StopInstancesRequest {
            this.instanceIds = instanceIds
        }

        ec2Client.stopInstances(request)
    }

    suspend fun rebootInstances(instanceIds: List<String>) {
        val request = RebootInstancesRequest {
            this.instanceIds = instanceIds
        }

        ec2Client.rebootInstances(request)
    }

    suspend fun terminateInstances(instanceIds: List<String>) {
        val request = TerminateInstancesRequest {
            this.instanceIds = instanceIds
        }

        ec2Client.terminateInstances(request)
    }
}