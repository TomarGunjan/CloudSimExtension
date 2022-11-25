package configs

import com.typesafe.config.ConfigFactory

/**
 * This is a config class for obtaining config values for Host config key
 * @param cloudModel
 */

class GetHostConfig (cloudModel:String){
  val config = ConfigFactory.load(cloudModel)
  val ram = config.getInt("host.ram")
  val bw = config.getInt("host.bw")
  val storage = config.getInt("host.storage")
  val numberOfPes = config.getInt("host.numberOfPe")
  val mipsCapacity = config.getInt("host.mipsCapacity")
}
