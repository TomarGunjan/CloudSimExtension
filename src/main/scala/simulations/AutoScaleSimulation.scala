package simulations

import helpers.{CreateLogger, DCConfigHelper}
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudsimplus.listeners.EventInfo
import org.cloudsimplus.builders.tables.CloudletsTableBuilder

import java.util
import scala.collection.JavaConverters.*


/** This is a class for simulating Auto Scaling PEs up and down acoording to incoming cloudlets
 */

class AutoScaleSimulation {

  val logger = CreateLogger(classOf[AutoScaleSimulation])

  //creating vms
  val autoscaleDatacenterutil = new DCConfigHelper("Autoscaling")
  val vmList = autoscaleDatacenterutil.createScalableVms()
  
  def start() = {
    logger.info("Starting Autoscaling simulation")


    //Creating cloudsim instance
    logger.info("creating cloudsim instance")
    val cloudsim = new CloudSim
    cloudsim.addOnClockTickListener(onClockTickListener)
    // Create a datacenter instance
    logger.info("creating datacenter")
    val datacenter = autoscaleDatacenterutil.createDatacenter(cloudsim)
    //setting scheduling interval to 1
    datacenter.setSchedulingInterval(1)
    //creating broker
    logger.info("creating broker")
    val broker0 = new DatacenterBrokerSimple(cloudsim)


    //creating cloudletlist
    logger.info("creating cloudlet list with different delays")
    val cloudletList = autoscaleDatacenterutil.createCloudletsWithDifferentDelays()
    // submitting vm list

    broker0.submitVmList(vmList.asJava)
    //submitting vm list
    broker0.submitCloudletList(cloudletList)

    // Start the simulation
    logger.info("starting simulation")
    cloudsim.start()

    // Build the simulation table
    val finishedCloudlet: util.List[Cloudlet] = broker0.getCloudletFinishedList()
    CloudletsTableBuilder(finishedCloudlet).build()
    
    logger.info(s"Finished execution of autoscale models simulation.")
  }

  //creating a listener for logging updates for each event
  def onClockTickListener(evt: EventInfo) = {
    vmList.foreach(vm =>
      printf(s"\t\tTime %6.1f: Vm %d CPU Usage: %6.2f%% (%2d vCPUs. Running Cloudlets: #%d). RAM usage: %.2f%% (%d MB)%n",
      evt.getTime, vm.getId, vm.getCpuPercentUtilization * 100.0, vm.getNumberOfPes,
      vm.getCloudletScheduler.getCloudletExecList.size,
      vm.getRam.getPercentUtilization * 100, vm.getRam.getAllocatedResource)
    )
  }

}
