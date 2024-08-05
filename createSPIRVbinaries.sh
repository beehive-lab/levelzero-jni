

openclSources=("constantsParameters copyData copyLong copyTest lookUpBufferAddress")

for kernel in $openclSources
do
	clang -cc1 -triple spir $kernel.cl -O2 -finclude-default-header -emit-llvm-bc -o $kernel.bc
	llvm-spirv $kernel.bc -o $kernel.spv
	echo "Creating SPIR-V Binary: $kernel.spv"
	rm $kernel.bc
done

