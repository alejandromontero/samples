#!/bin/bash


RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[1;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

PYTHON_PACKAGES="stem pycurl"
PYTHON_PACKAGES_INSTALLED=""
PACKAGES=""

install_dependencies() {
	echo -e "$BLUE checking dependencies... $NC \n"

	if [ ! -d $(pwd)/tor-browser_en-US ]; then
        echo -e "$RED TOR not found in ${NC} $(pwd) ${RED} Downloading... $NC \n"
        wget https://github.com/TheTorProject/gettorbrowser/releases/download/v6.5.2/tor-browser-linux64-6.5.2_en-US.tar.xz
        echo -e"$BLUE Untaring TOR $NC \n"
	    tar -xvf tor-browser-linux64-6.5.2_en-US.tar.xz
		PACKAGES+=" tor"
	fi

	if ! dpkg --get-selections | grep "^python[[:space:]]*install$" &> /dev/null; then
		echo -e "$RED Python not found in system, you need to download it first... $NC \n"
		exit 1
	fi

	for package in $PYTHON_PACKAGES; do

		python -c "import $package" &> /dev/null ||  if [ $(echo $?) ]; then
            echo -e "$BLUE Installing Python module $pacakge $NC \n"
        	pip install --user $package
        	PYTHON_PACKAGES_INSTALLED+=" $package"
		fi
	done
}

uninstall_dependencies() {
	echo -e "$RED uninstalling previously installed packages for this test: $PYTHON_PACKAGES $PACKAGES  $NC \n"

	for package in $PYTHON_PACKAGES_INSTALLED; do
        pip uninstall -y $package
	done

	for package in $PACKAGES; do
		find -name "$package*" -exec rm -r {} \; &> /dev/null
	done

	echo -e "$BLUE uninstall completed $NC \n"
}

#MAIN SECTION

echo -e "${GREEN} ----------------Starting TOR evaluation script---------------- $NC \n"

if [ -z $1 ] ; then
    echo -e "$YELLOW WARNING: Using default number of iterations=10 $NC \n"
    n_iter=10
else
    echo -e "$YELLOW WARNING: Using number of iterations=$1 $NC \n"
    n_iter=$1
fi

install_dependencies

echo -e "$BLUE starting TOR browser for the fist time, please select Connect option and close the browser $NC \n"
$(pwd)/tor-browser_en-US/Browser/start-tor-browser
$(pwd)/tor-browser_en-US/Browser/start-tor-browser &

echo -e "$YELLOW WARNING: Sleeping for 10 seconds to wait for TOR to initiate... $NC \n"
sleep 10

echo -e "$GREEN Starting the experiments... now $NC \n"
python $(pwd)/script.py $n_iter

#uninstall_dependencies



