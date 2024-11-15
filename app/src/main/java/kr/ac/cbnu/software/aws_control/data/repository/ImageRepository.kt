package kr.ac.cbnu.software.aws_control.data.repository

import aws.sdk.kotlin.services.ec2.Ec2Client
import aws.sdk.kotlin.services.ec2.model.DescribeImagesRequest
import aws.sdk.kotlin.services.ec2.model.DescribeImagesRequest.Companion.invoke
import aws.sdk.kotlin.services.ec2.model.Filter
import aws.sdk.kotlin.services.ec2.model.Filter.Companion.invoke
import aws.sdk.kotlin.services.ec2.model.Image
import javax.inject.Inject

internal class ImageRepository @Inject constructor(
    private val ec2Client: Ec2Client
) {
    suspend fun getImages(nameFilter: List<String>): List<Image> {
        val request = DescribeImagesRequest {
            filters = listOf(
                Filter {
                    name = "name"
                    values = nameFilter
                }
            )
        }

        val response = ec2Client.describeImages(request)
        return response.images ?: emptyList()
    }
}