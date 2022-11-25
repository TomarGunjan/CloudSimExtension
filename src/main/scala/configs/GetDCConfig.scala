package configs

import com.typesafe.config.ConfigFactory

/**
 * This is a utility class for obtaining config for datacenter config key
 * @param cloudModel
 */
class GetDCConfig(cloudModel : String){
  val config = ConfigFactory.load(cloudModel)
  val numberOfHosts = config.getInt("datacenter.numOfHosts")
  val numOfCloudlets = config.getInt("datacenter.numofCloudlets")
  val numOfVms = config.getInt("datacenter.numOfVms")
  val arch = config.getString("datacenter.arch")
  val os = config.getString("datacenter.os")
  val vmm = config.getString("datacenter.vmm")
  val costPerSec = config.getDouble("datacenter.costPerSec")
  val costPerBw = config.getDouble("datacenter.costPerBw")
  val costPerMem = config.getDouble("datacenter.costPerMem")
  val costPerStorage = config.getDouble("datacenter.costPerStorage")
}
