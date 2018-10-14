#!/bin/bash
#
#SBATCH --partition=piv
#SBATCH --job-name=phase2
#SBATCH --time=4:00
#SBATCH --output=out/phase2.%j.out
#SBATCH --ntasks=11
#

echo "---------- Preparing environment -------------"
module purge
module load prun gnu7/7.3.0 openmpi3/3.0.0 py2-mpi4py/3.0.0
echo "---------- Prepared environment -------------"

echo "------- Start of parallel processes ------"
time prun python phase2.py
echo "------- End of parallel processes ------"