#import "TorchMobilePlugin.h"
#import <torch_mobile/torch_mobile-Swift.h>

@implementation TorchMobilePlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftTorchMobilePlugin registerWithRegistrar:registrar];
}
@end
