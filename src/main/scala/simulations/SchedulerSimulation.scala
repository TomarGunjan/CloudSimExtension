package simulations

import helpers.{CreateLogger, DCConfigHelper}
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletScheduler
import org.cloudbus.cloudsim.schedulers.vm.VmScheduler
import org.cloudsimplus.builders.tables.CloudletsTableBuilder
import org.cloudbus.cloudsim.cloudlets.{Cloudlet}

import scala.collection.JavaConverters.*
import java.util

/** A class to simulate Time shared and Space shared VM and Cloudlet scheduling policies.
 *
 * @param scheduleModel
 * @param vmScheduler
 * @param cloudletScheduler
 *
 * This class is for simulating Scheduler
 */

class SchedulerSimulation(scheduleModel: String, vmScheduler: VmScheduler, cloudletScheduler: CloudletScheduler){

  val logger = CreateLogger(classOf[AutoScaleSimulation])

  def start() = {

    // Creating cloudsim instance
    logger.info("Creating cloud sim instance")
    val cloudsim = new CloudSim

    //Creating config util
    val schedulerDatacenterutil = new DCConfigHelper(scheduleModel:String,vmScheduler: VmScheduler, cloudletScheduler: CloudletScheduler)

    //creating datacenter
    logger.info("Creating datacenter")
    val datacenter = schedulerDatacenterutil.createDatacenter(cloudsim)
    val broker = new DatacenterBrokerSimple(cloudsim)
    logger.info("Creating VMs")
    val vmList = schedulerDatacenterutil.createVms()

    // Create a list of cloudlets
    logger.info("Creating cloudlets")
    val cloudletList = schedulerDatacenterutil.createCloudlets()


    // Submit VMs and cloudlets to broker
    logger.info("Submitting VMs and cloudlets to broker")
    broker.submitVmList(vmList.asJava)
    broker.submitCloudletList(cloudletList.asJava)

    // Start the simulation
    cloudsim.start()

    // Build the simulation table
    val finishedCloudlet = broker.getCloudletFinishedList()
    CloudletsTableBuilder(finishedCloudlet).build()

    val scalaCloudletList: List[Cloudlet] = finishedCloudlet.asScala.toList
    scalaCloudletList.map(cloudlet => {
      val cloudletId = cloudlet.getId
      val cost = cloudlet.getTotalCost()
      val dc = cloudlet.getLastTriedDatacenter()
      logger.info(s"Cost of cloudlet: $cloudletId on datacenter $dc is $cost")
    }
    )

    logger.info(s"Finished execution of $scheduleModel VM and cloudlet scheduling policy.")
  }

}
