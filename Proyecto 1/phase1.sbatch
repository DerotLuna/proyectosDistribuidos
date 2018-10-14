#!/bin/bash
#
#SBATCH --partition=all
#SBATCH --job-name=phase1
#SBATCH --time=4:00
#SBATCH --output=out/phase1.%j.out
#SBATCH --ntasks=11
#

echo "---------- Preparing environment -------------"
module purge
module load prun gnu7/7.3.0 openmpi3/3.0.0 py2-mpi4py/3.0.0
echo "---------- Prepared environment -------------"

echo $(hostname)"---------- Exporting environment variables -------------"
export LOCAL_HOME=/local_home/$USER
export WP=$(pwd)
echo $(hostname)"---------- Exported environment variables -------------"

echo $(hostname)"---------- Copying book in the nodes -------------"
echo $(hostname)": Transferring file $WP/data to $LOCAL_HOME in the nodes $SLURM_NODELIST"
PDSH_SSH_ARGS_APPEND="-o StrictHostKeyChecking=no" pdsh -w $SLURM_NODELIST cp $WP/data/libro_medicina.txt $LOCAL_HOME
echo $(hostname)"---------- Copied book in the nodes --------------"

echo "------- Start of parallel processes ------"
time prun python phase1.py
echo "------- End of parallel processes -------"

echo $(hostname)"------- Deleting book in the nodes ------"
PDSH_SSH_ARGS_APPEND="-o StrictHostKeyChecking=no" pdsh -w $SLURM_NODELIST rm $LOCAL_HOME/libro_medicina.txt
echo $(hostname)"------- Deleted book in the nodes -------"

#http://www.softpanorama.org/Admin/Parallel_execution_tools/pdsh.shtml