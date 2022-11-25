package configs

import com.typesafe.config.ConfigFactory
import org.cloudbus.cloudsim.utilizationmodels.UtilizationModelDynamic

/** This is a utility class for on obtaining values for Cloudlet config key */

class GetCloudletConfig (cloudModel:String){
  val config = ConfigFactory.load(cloudModel)
  val length = config.getInt("cloudlet.length")
  val pesNumber = config.getInt("cloudlet.pesNumber")
  val utilizationModel = new UtilizationModelDynamic(config.getDouble("cloudlet.utilizationModel"))
  val size = config.getInt("cloudlet.size")
}
