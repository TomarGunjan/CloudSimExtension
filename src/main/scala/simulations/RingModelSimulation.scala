package simulations

import com.typesafe.config.ConfigFactory
import helpers.{CreateLogger, DCConfigHelper}
import org.cloudbus.cloudsim.allocationpolicies.{VmAllocationPolicy, VmAllocationPolicyRoundRobin, VmAllocationPolicySimple}
import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.{Cloudlet, CloudletSimple}
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.DatacenterSimple
import org.cloudbus.cloudsim.hosts.HostSimple
import org.cloudbus.cloudsim.network.topologies.BriteNetworkTopology
import org.cloudbus.cloudsim.provisioners.ResourceProvisionerSimple
import org.cloudbus.cloudsim.resources.{Pe, PeSimple}
import org.cloudbus.cloudsim.schedulers.cloudlet.{CloudletScheduler, CloudletSchedulerAbstract, CloudletSchedulerSpaceShared, CloudletSchedulerTimeShared}
import org.cloudbus.cloudsim.schedulers.vm
import org.cloudbus.cloudsim.schedulers.vm.{VmScheduler, VmSchedulerAbstract, VmSchedulerSpaceShared, VmSchedulerTimeShared}
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic
import org.cloudbus.cloudsim.vms.VmSimple
import org.cloudsimplus.builders.tables.CloudletsTableBuilder

import java.util
import scala.collection.JavaConverters.*

/**
 *  This class is for simulating different cloud models including Iaas Paas and Saas connected in ring topology
 */

class RingModelSimulation {

  /** In Megabits/s. */
  private val NETWORK_BW = 10.0

  /** In seconds. */
  private val NETWORK_LATENCY = 10.0

  val logger = CreateLogger(classOf[RingModelSimulation])

  def start() = {
    logger.info(s"Starting execution of cloud models simulation.")

    // Creating cloud sim instance
    logger.info("Creating cloud sim instance")
    val cloudsim = new CloudSim

    // Creating config utils for differetn cloud models
    val iaasDCConfigutil = new DCConfigHelper("Iaas")
    val paasDCConfigutil = new DCConfigHelper("Paas")
    val saasDCConfigutil = new DCConfigHelper("Saas")

    // Creating different datacenters for  cloud models Saas, Paas and Iaas.
    logger.info("Creating datacenters for Iaas, Paas, Saas")
    val iaasDatacenter = iaasDCConfigutil.createDatacenter(cloudsim)
    val paasDatacenter = paasDCConfigutil.createDatacenter(cloudsim)
    val saasDatacenter = saasDCConfigutil.createDatacenter(cloudsim)

    // Creating broker instance
    logger.info("Creating broker instance")
    val broker = new DatacenterBrokerSimple(cloudsim)

    // Connect the datacenters and broker in a BRITE Network topology using the topology.brite file.
    logger.info("Setting up netwrok topology")
    val networkTopology = new BriteNetworkTopology()
    cloudsim.setNetworkTopology(networkTopology)

    //Connecting datacenters and brokers in ring topology
    networkTopology.addLink(iaasDatacenter,paasDatacenter,NETWORK_BW, NETWORK_LATENCY)
    networkTopology.addLink(paasDatacenter,saasDatacenter, NETWORK_BW, NETWORK_LATENCY)
    networkTopology.addLink(saasDatacenter,broker, NETWORK_BW, NETWORK_LATENCY)
    networkTopology.addLink(broker,iaasDatacenter, NETWORK_BW, NETWORK_LATENCY)




    // Create a list of VMs for all datacenters
    logger.info("Creating VMs")
    val iaasVmList = iaasDCConfigutil.createVms()
    val paasVmList = paasDCConfigutil.createVms()
    val saasVmList = saasDCConfigutil.createVms()
    val allVmList = iaasVmList ::: paasVmList ::: saasVmList

    // Create a list of cloudlets for all datacenters
    logger.info("Creating cloudlets")
    val iaasCloudletList = iaasDCConfigutil.createCloudlets()
    val paasCloudletList = paasDCConfigutil.createCloudlets()
    val saasCloudletList = saasDCConfigutil.createCloudlets()
    val allCloudletList = iaasCloudletList ::: paasCloudletList ::: saasCloudletList

    // Submit VMs and cloudlets to broker
    logger.info("Submitting VMs and cloudlets to broker")
    broker.submitVmList(allVmList.asJava)
    broker.submitCloudletList(allCloudletList.asJava)

    // Start the simulation
    cloudsim.start()

    val finishedCloudlet: util.List[Cloudlet] = broker.getCloudletFinishedList()
    CloudletsTableBuilder(finishedCloudlet).build()

    val scalaCloudletList: List[Cloudlet] = finishedCloudlet.asScala.toList.sorted
    scalaCloudletList.map(cloudlet => {
      val cloudletId = cloudlet.getId
      val cost = cloudlet.getTotalCost()
      val dc = cloudlet.getLastTriedDatacenter()
      logger.info(s"Cost of cloudlet: $cloudletId on datacenter $dc is $cost")
    }
    )
    logger.info(s"Finished execution of cloud models simulation.")
  }
}

