package simulations

import helpers.{CreateLogger, DCConfigHelper}
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudsimplus.builders.tables.CloudletsTableBuilder

import scala.collection.JavaConverters.*

/**
 * This class is for simulating RoundRobin policy for VM Allocation
 * @param schedulerModel
 * @param vmAllocation
 */

class VmAllocPolRoundRobin(schedulerModel: String, vmAllocation: VmAllocationPolicy) {
  val logger =CreateLogger(classOf[VmAllocPolRoundRobin])

  def start() = {
    logger.info("Starting simulation for VMAllocation Round Robin policy")

    //Creating cloudsim instance
    logger.info("Creating Cloudsim instance")
    val cloudsim = new CloudSim

    val aPConfig =new DCConfigHelper("RoundRobin")
    //creating datacenter
    logger.info("Creating datacenter")
    val datacenter = aPConfig.createDatacenter(cloudsim)

    //creating broker
    logger.info("Creating broker")
    val broker =new DatacenterBrokerSimple(cloudsim)

    //Creating vmlist
    logger.info("Creating vm list")
    val vmList = aPConfig.createVms()

    //Creating cloudlet list
    logger.info("Creating cloudletlist")
    val cloudletList =aPConfig.createCloudlets()

    //submitting vmlist to broker
    broker.submitVmList(vmList.asJava)
    //submitting cloudlist to broker
    broker.submitCloudletList(cloudletList.asJava)

    //starting simulation
    cloudsim.start()

  //Build simulation table
    val finishedCloudlet = broker.getCloudletFinishedList()
    CloudletsTableBuilder(finishedCloudlet).build()
    logger.info(s"Finished execution of $schedulerModel VM ALlocation Policy.")
  }
}
